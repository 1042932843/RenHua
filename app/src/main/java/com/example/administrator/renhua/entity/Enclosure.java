package com.example.administrator.renhua.entity;

/**
 * Created by Administrator on 2017/4/19 0019.
 */
public class Enclosure {
    private String name;
    private String filling_requirement;
    private String copy_check_num;
    private String origin_check_num;
    private String origin;
    private String copy_accept_num;
    private String is_situation;
    private String creator;
    private String description;
    String origin_accept_num;
    String last_modification_time;
    String creation_time;

    public String getFilling_requirement() {
        return filling_requirement;
    }

    public void setFilling_requirement(String filling_requirement) {
        this.filling_requirement = filling_requirement;
    }

    public String getCopy_check_num() {
        return copy_check_num;
    }

    public void setCopy_check_num(String copy_check_num) {
        this.copy_check_num = copy_check_num;
    }

    public String getOrigin_check_num() {
        return origin_check_num;
    }

    public void setOrigin_check_num(String origin_check_num) {
        this.origin_check_num = origin_check_num;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCopy_accept_num() {
        return copy_accept_num;
    }

    public void setCopy_accept_num(String copy_accept_num) {
        this.copy_accept_num = copy_accept_num;
    }

    public String getIs_situation() {
        return is_situation;
    }

    public void setIs_situation(String is_situation) {
        this.is_situation = is_situation;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrigin_accept_num() {
        return origin_accept_num;
    }

    public void setOrigin_accept_num(String origin_accept_num) {
        this.origin_accept_num = origin_accept_num;
    }

    public String getLast_modification_time() {
        return last_modification_time;
    }

    public void setLast_modification_time(String last_modification_time) {
        this.last_modification_time = last_modification_time;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getLast_modificator() {
        return last_modificator;
    }

    public void setLast_modificator(String last_modificator) {
        this.last_modificator = last_modificator;
    }

    public String getMaterials_code() {
        return materials_code;
    }

    public void setMaterials_code(String materials_code) {
        this.materials_code = materials_code;
    }

    public String getMaterials_type() {
        return materials_type;
    }

    public void setMaterials_type(String materials_type) {
        this.materials_type = materials_type;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    String sort_order;
    String last_modificator;
    String materials_code;
    String materials_type;
    String copy;

    public String getEmpty() {
        return empty;
    }

    public void setEmpty(String empty) {
        this.empty = empty;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    String empty;//空白网盘ID

    public String getEmpty_name() {
        return empty_name;
    }

    public void setEmpty_name(String empty_name) {
        this.empty_name = empty_name;
    }

    public String getSample_name() {
        return sample_name;
    }

    public void setSample_name(String sample_name) {
        this.sample_name = sample_name;
    }

    String empty_name;
    String sample;//样表网盘ID
    String sample_name;

    public String getMaterials_name() {
        return materials_name;
    }

    public void setMaterials_name(String materials_name) {
        this.materials_name = materials_name;
    }

    private String materials_name;
    private String fileUrl;
    private String fileName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
