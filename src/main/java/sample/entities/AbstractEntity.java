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
     * entity�� ������ copy�� ���� helper method
     * <br>
     * AbstractVOEntity�� ����� VO entity�� ��� db locale ������ �߰������� �������ش�.
     * get/set �޼��� ���� ���� ���� super.copyTo �޼��忡 ���� �ڵ����簡 �ȴ�.
     * �ٸ� ��쿡�� �޼��� ������ ���� ����߸� �Ѵ�.
     * <font clolor='red'>super.copyTo(class)�� db locale ������ �⺻������ �����ϹǷ� �⺻������ ȣ���ϴ� ���� �ٶ����ϴ�.</font>
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
