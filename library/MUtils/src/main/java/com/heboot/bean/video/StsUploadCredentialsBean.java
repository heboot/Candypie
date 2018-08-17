package com.heboot.bean.video;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/20.
 */

public class StsUploadCredentialsBean extends BaseBeanEntity {


    /**
     * config : {"AccessKeySecret":"569W32CfXdCoZchqGpuzsM7Hh9MyeWwpRgo848NLkntR","AccessKeyId":"STS.KaU8eTjfK9HmkKQCnjcnMitKd","Expiration":"2018-03-01T10:46:02Z","SecurityToken":"CAES9wIIARKAAUHJaQDks7pbrTJ8SQ44cx1wpDW7I9ba5g550S4R8ucgVCouVYiYzBiZCGu6jjvzTVW9WUlld4LdF8ouVoP5+ChZimgVH+dUSquh5Zzbm+HRpWbo8itf5kuJCQP2I3Myqy+V6iz2WqSamPY7eDMPPBY8FGVX0+qaAsTuh/jw4xinGh1TVFMuS2FVOGVUamZLOUhta0tRQ25qY25NaXRLZCISMzI5OTc0MTM2NDUyMDQ5NTMyKg91cGxvYWQtdmlkZW8tMTAwuPasiZ4sOgZSc2FNRDVCSgoBMRpFCgVBbGxvdxIbCgxBY3Rpb25FcXVhbHMSBkFjdGlvbhoDCgEqEh8KDlJlc291cmNlRXF1YWxzEghSZXNvdXJjZRoDCgEqShAxNjkzODkwMTQ4NTQ3MDY0UgUyNjg0MloPQXNzdW1lZFJvbGVVc2VyYABqEjMyOTk3NDEzNjQ1MjA0OTUzMnIIYXBpd3JpdGV4+PPexNiSgQM="}
     */

    private ConfigBean config;

    public ConfigBean getConfig() {
        return config;
    }

    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    public static class ConfigBean {
        /**
         * AccessKeySecret : 569W32CfXdCoZchqGpuzsM7Hh9MyeWwpRgo848NLkntR
         * AccessKeyId : STS.KaU8eTjfK9HmkKQCnjcnMitKd
         * Expiration : 2018-03-01T10:46:02Z
         * SecurityToken : CAES9wIIARKAAUHJaQDks7pbrTJ8SQ44cx1wpDW7I9ba5g550S4R8ucgVCouVYiYzBiZCGu6jjvzTVW9WUlld4LdF8ouVoP5+ChZimgVH+dUSquh5Zzbm+HRpWbo8itf5kuJCQP2I3Myqy+V6iz2WqSamPY7eDMPPBY8FGVX0+qaAsTuh/jw4xinGh1TVFMuS2FVOGVUamZLOUhta0tRQ25qY25NaXRLZCISMzI5OTc0MTM2NDUyMDQ5NTMyKg91cGxvYWQtdmlkZW8tMTAwuPasiZ4sOgZSc2FNRDVCSgoBMRpFCgVBbGxvdxIbCgxBY3Rpb25FcXVhbHMSBkFjdGlvbhoDCgEqEh8KDlJlc291cmNlRXF1YWxzEghSZXNvdXJjZRoDCgEqShAxNjkzODkwMTQ4NTQ3MDY0UgUyNjg0MloPQXNzdW1lZFJvbGVVc2VyYABqEjMyOTk3NDEzNjQ1MjA0OTUzMnIIYXBpd3JpdGV4+PPexNiSgQM=
         */

        private String AccessKeySecret;
        private String AccessKeyId;
        private String Expiration;
        private String SecurityToken;

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String AccessKeySecret) {
            this.AccessKeySecret = AccessKeySecret;
        }

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String AccessKeyId) {
            this.AccessKeyId = AccessKeyId;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String Expiration) {
            this.Expiration = Expiration;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String SecurityToken) {
            this.SecurityToken = SecurityToken;
        }
    }
}
