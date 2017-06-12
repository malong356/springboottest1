package com.lucky.springboottest1.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * mark
 * 引入依赖包
 * <pre>
 *     {@code
 *      <dependency>
 *           <groupId>org.apache.commons</groupId>
 *           <artifactId>commons-lang3</artifactId>
 *      </dependency>
 *      <dependency>
 *           <groupId>com.alibaba</groupId>
 *           <artifactId>fastjson</artifactId>
 *      </dependency>
 *     }
 * </pre>
 * <p>Created by aiping.yuan on 2016/12/9.<p>
 */
public class MarkToString extends ReflectionToStringBuilder {

    private static volatile ToStringStyle defaultStyle = ToStringStyle.SHORT_PREFIX_STYLE;
    private static final char SEPARATOR_CHAR_ASTERISK = '*';
    private static final int TO_STRING_MASK_SIZE_THRESHOLD = 50;
    private static final String ALL_ASTERISK = "******";
    private static final String NULL = "null";



    public MarkToString(Object object) {
        super(object);
    }

    public MarkToString(Object object, ToStringStyle style) {
        super(object, style);
    }

    public static String markToJSON(String json) {
        try {
            Map map = JSON.parseObject(json, new TypeReference<Map>(){});
            return markToMap(map);
        } catch (JSONException e) {
            //- skiping 不是json，返回原数据
        }
        return json;
    }

    public static String markToMap(Map map) {
        MarkData data = new MarkData();
        data.setMap(map);
        return toString(data);
    }

    public static class MarkData implements Serializable {
        private static final long serialVersionUID = -165781014651503289L;
        @Mark(maskKeys = {"userName", "userPhone", "userIdCard", "bankCardNo", "liveDetailAddress", "livePhone", "unitPhone"
                , "unitDetailAddress", "unitAddress", "bearUserName", "bearUserIdCard", "contactList", "extendsData", "name", "noCer"
                , "cardNo", "certNo", "idNo", "cellphone", "email", "abodeDetail", "empAddr", "empPhone", "baseInfo"
                , "occupationInfo", "addressList", "additionalList", "mobile", "cardHolderName", "cardHolderId"
                , "phoneNo", "idenCard", "loanUserMobile", "loanUserName", "idCard", "conName1", "conName2", "conPhone1"
                , "conPhone2", "phone", "bankAcctNbr", "bankAcctName", "mobileNo", "bankAccountName"
                , "payResultList", "usrName", "certId", "reqMap","empName","compName","compOfficePhone","custName","realName"
                ,"bankPhone","contactName","contactMobile","customerName"}, subKeys = {"data", "service_param", "basic","content"})
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }

    public static String toString(Object o) {
        return new MarkToString(o, defaultStyle).toString();
    }

    @Override
    protected void appendFieldsIn(Class<?> clazz) {
        if (clazz.isArray()) {
            this.reflectionAppendArray(this.getObject());
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for(Field field : fields){
            String fieldName = field.getName();
            if (this.accept(field)) {
                Object fieldValue = null;
                try {
                    fieldValue = this.getValue(field);
                } catch (IllegalAccessException e) {
                    //- skiping
                }
                if (null == fieldValue) {
                    continue;
                }
                Hide hide = field.getAnnotation(Hide.class);
                if (null != hide) {
                    continue;
                }
                try {
                    if (field.getType() == String.class) {
                        fieldValue = buildMarkString(field, fieldValue);
                    } else if (field.getType() == Map.class) {
                        fieldValue = buildMarkMap(field, fieldValue);
                    } else if (field.getType() == Object.class) {
                        fieldValue = buildMarkObject(field, fieldValue);
                    }
                } catch (Exception e) {
                    //- 异常输入原有数据
                }
                this.append(fieldName, fieldValue);
            }
        }
    }

    private static Object buildMarkString(Field field, Object fieldValue) {
        String str = (String) fieldValue;
        Mark mark = field.getAnnotation(Mark.class);
        if (null == mark) {
            return fieldValue;
        } else if (mark.all()) {
            return maskAll(str);
        } else {
            return mask(str);
        }
    }

    private static Object buildMarkMap(Field field, Object fieldValue) {
        Map map = (Map) fieldValue;
        Mark mark = field.getAnnotation(Mark.class);
        if (null == mark) {
            return fieldValue;
        } else if (mark.all()) {
            return ALL_ASTERISK;
        } else if (null != mark.maskKeys() && mark.maskKeys().length > 0) {
            return buildMarkMap(mark, map, TO_STRING_MASK_SIZE_THRESHOLD);
        }
        return fieldValue;
    }

    private static String buildMarkObject(Field field, Object fieldValue) {
        Mark mark = field.getAnnotation(Mark.class);
        if (null == mark) {
            return fieldValue.toString();
        } else if (mark.all()) {
            fieldValue = ALL_ASTERISK;
        } else if (null != mark.maskKeys() && mark.maskKeys().length > 0) {
            Method[] methods = fieldValue.getClass().getDeclaredMethods();
            if (methods.length == 0) {
                return fieldValue.toString();
            }
            boolean isToString = false;
            for(Method method : methods){
                if (equals("toString", method.getName())) {
                    isToString = true;
                    break;
                }
            }
            if (!isToString) {
                Map map = JSON.parseObject(JSON.toJSONString(fieldValue), new TypeReference<Map>(){});
                return buildMarkMap(mark, map, TO_STRING_MASK_SIZE_THRESHOLD).toString();
            }
        }
        return fieldValue.toString();
    }

    private static String buildMarkMap(Mark mark, Map map, int size) {
        List<String> maskKeys = Arrays.asList(mark.maskKeys());
        List<String> subKeys = Arrays.asList(mark.subKeys());
        return processMap(maskKeys, subKeys, map, TO_STRING_MASK_SIZE_THRESHOLD);
    }

    private static String processMap(List<String> maskKeys, List<String> subKeys, Map map, int size) {
        if (null == map) {
            return NULL;
        }
        Iterator<Map.Entry> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(10 * Math.min(size, map.size()));
        sb.append('{');
        for (int i = 0; i < size; i++) {
            Map.Entry e = it.next();
            Object key = e.getKey();
            Object value = e.getValue();
            sb.append(key);
            sb.append('=');
            if (null != maskKeys && maskKeys.contains(key)) {
                String maskValue = NULL;
                if (value != NULL) {
                    maskValue = mask(value.toString());
                }
                sb.append(value == map ? "(this Map)" : maskValue);
            } else if ((null != subKeys && subKeys.contains(key))) {
                String maskValue = NULL;
                if (value.getClass() == JSONObject.class) {
                     maskValue = processMap(maskKeys, subKeys, (Map) value, size);
                } else if (value.getClass() == JSONArray.class) {
                    maskValue = processCollection(maskKeys, subKeys, (List) value, size);
                } else {
                    maskValue = mask(value.toString());
                }
                sb.append((value == map) ? "(this Map)" : maskValue);
            } else {
                sb.append((value == map) ? "(this Map)" : value);
            }
            if (!it.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',');
        }
        sb.append("******...}");
        return sb.toString();
    }

    private static String processCollection(List<String> maskKeys, List<String> subKeys, Collection collection, int size) {
        if (collection == null) {
            return NULL;
        }
        Iterator it = collection.iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder(10 * Math.min(size, collection.size()));
        sb.append('[');
        for (int i = 0; i < size; i++) {
            Object e = it.next();
            if (null != e && e.getClass() == JSONObject.class) {
                sb.append(processMap(maskKeys, subKeys, (Map) e, size));
            } else {
                sb.append(e == collection ? "(this Collection)" : e);
            }
            if (!it.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',');
        }
        sb.append("...]");
        return sb.toString();
    }

    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public static @interface Mark {
        /** 只支持String */
        boolean all() default false;

        /** 需要mask的key值 */
        String[] maskKeys() default {};

        /**
         * 会存在这种情况
         * <pre>
         *     {
         *        message=操作成功,
         *        data={
         *            "cardNo": "6222222222222222222",
         *            "mobile": "13300000000"
         *            },
         *        code=0
         *     }
         *     变为
         *     {
         *        message=操作成功,
         *        data={
         *            "cardNo": "62222*********22222",
         *            "mobile": "133*****000"
         *            },
         *        code=0
         *     }
         * </pre>
         * @return
         */
        String[] subKeys() default {"data"};
    }

    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public static @interface Hide {

    }

    /**
     * 隐藏字符串
     * @param str
     * @return
     */
    public static String mask(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        int len = str.length();
        if (1 == len) {
            return String.valueOf(SEPARATOR_CHAR_ASTERISK);
        }
        int maskLen = 0;
        int begin = 0;
        // -> 特殊处理：针对身份证号
        if(len >= 16 && len <= 22) {
            maskLen = len - 6 - 4;
            begin = 6;
        }
        // -> 特殊处理：针对手机号
        else if (len == 11) {
            maskLen = len - 3 - 4;
            begin = 3;
        } else {
            if (len > TO_STRING_MASK_SIZE_THRESHOLD) {
                str = "******...";
            }
            len = str.length();
            maskLen = Math.max((len / 2), 1);
            begin = (len - maskLen) / 2;
        }
        char[] chars = str.toCharArray();
        char[] mask = repeatAsterisk(maskLen);
        System.arraycopy(mask, 0, chars, begin, maskLen);
        return new String(chars);
    }

    public static String maskAll(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return ALL_ASTERISK;
    }

    private static char[] repeatAsterisk(int len) {
        char[] chars = new char[len];
        Arrays.fill(chars, SEPARATOR_CHAR_ASTERISK);
        return chars;
    }

    private static boolean equals(String var1, String var2) {
        if (null == var1 || null == var2) {
            return false;
        }
        if (var1.equals(var2)) {
            return true;
        }
        return false;
    }

}
