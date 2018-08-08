package sample.entities;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractEntity implements Serializable {

    /**
     * execute toJSON() internally. if error occured during toJSON then use commons lang3 ToStringBuilder
     */
    @Override
    public String toString() {
        return this.toJSON();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    /**
     * comparizon by convered string using ToStringBuilder.reflectionToString
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
                
        String source = substringForEquals(ToStringBuilder.reflectionToString(this));
        String target = substringForEquals(ToStringBuilder.reflectionToString(obj));
        return source.equals(target);
    }
    
    private String substringForEquals(String value) {
        int idx = value.indexOf('[');
        if (idx < 1) {
            return value;
        }
        return value.substring(idx, value.length());
    }
    
    /**
     * convert JSON format string using jackson json 
     * 
     * @return return JSON formatted string 
     */
    public String toJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        String jsonInString;
        try {
            jsonInString = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.warn("error occured during parse to json:" + e.getMessage(), e);
            return "{"+ToStringBuilder.reflectionToString(this)+"}";
        }

        return jsonInString;
    }
    
    /**
     * entity간 데이터 copy를 위한 helper method
     * <br>
     * AbstractVOEntity를 상속한 VO entity의 경우 db locale 정보를 추가적으로 세팅해준다.
     * get/set 메서드 명이 같은 경우는 super.copyTo 메서드에 의해 자동복사가 된다.
     * 다를 경우에는 메서드 매핑을 각각 해줘야만 한다.
     * <font clolor='red'>super.copyTo(class)는 db locale 정보를 기본적으로 세팅하므로 기본적으로 호출하는 것이 바람직하다.</font>
     * exmaple :
     * copyToVO() {
     *     SampleBoardVO vo = super.copyTo(SampleBoardVO.class);
     *     
     *     vo.setField1(getField1());
     *     vo.setField2(getField2());
     *     ...
     *     
     *     return vo;
     * }
     * 
     * @see org.apache.commons.beanutils.copyProperties(Object dest, Object orig)
     * @param type 
     * @return
     */
    public <T> T copyTo(Class<T> type) {
        try {
            T instance = type.newInstance();
            if (AbstractEntity.class.isAssignableFrom(type) || AbstractPersistable.class.isAssignableFrom(type)) {
                BeanUtils.copyProperties(instance, this);
            } else {
                throw new IllegalArgumentException("only support AbstractEntity's child entity");
            }
            return instance;
            
        } catch (Exception e) {
            throw new RuntimeException("copy Entity fail", e);
        }
    }
}
