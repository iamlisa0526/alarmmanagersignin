package com.example.renliqian.alarmmanagersignin.modle;

import java.io.Serializable;
import java.util.List;

/**
 * 签到记录 QO
 *
 * @author May
 * @version 1.0
 * @date 2019/3/25 17:08
 */
public class SignInRecordQo implements Serializable {

    private static final long serialVersionUID = -7339371589707603785L;

    /**
     * 此次签到所对应的签到设置的id
     */
    private Long setId;

    /**
     * 签到学生账号
     */
    private String studentUsername;

    /**
     * 学生的班级信息
     */
    private ClassQo classQo;

    /**
     * 此次签到的设备Mac地址
     */
    private String macAddress;

    /**
     * 此次签到对应的学生的姓名
     */
    private String studentName;

    /**
     * 签到的地点的经度
     */
    private Double longitude;

    /**
     * 签到的地点的纬度
     */
    private Double latitude;


    public Long getSetId() {
        return setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public ClassQo getClassQo() {
        return classQo;
    }

    public void setClassQo(ClassQo classQo) {
        this.classQo = classQo;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 班级 QO
     *
     * @author May
     * @version 1.0
     * @date 2019/3/25 13:45
     */
    public static class ClassQo implements Serializable {

        private static final long serialVersionUID = -7339371589707603785L;

        /**
         * 班级的名称
         */
        private String className;

        /**
         * 学校的名称
         */
        private String schoolName;

        /**
         * 班级下的设备信息（学生信息）
         */
        List<EquipmentQo> equipmentList;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public List<EquipmentQo> getEquipmentList() {
            return equipmentList;
        }

        public void setEquipmentList(List<EquipmentQo> equipmentList) {
            this.equipmentList = equipmentList;
        }

        /**
         * 学生的设备信息 QO
         *
         * @author 10440
         * @version 1.0
         * @date 2019/5/28 10:05
         */
        public static class EquipmentQo implements Serializable {

            private static final long serialVersionUID = -7339371589707603785L;

            /**
             * 用户的登录名
             */
            private String name;

            /**
             * 学生的名字
             */
            private String studentName;

            /**
             * 设备的Mac地址
             */
            private String macAddress;


            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public String getMacAddress() {
                return macAddress;
            }

            public void setMacAddress(String macAddress) {
                this.macAddress = macAddress;
            }
        }
    }
}


