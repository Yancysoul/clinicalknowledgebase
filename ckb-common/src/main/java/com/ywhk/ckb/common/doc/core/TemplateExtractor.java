package com.ywhk.ckb.common.doc.core;

import com.ywhk.ckb.common.doc.domain.ParamInfo;
import com.ywhk.ckb.common.doc.domain.PropertyVO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class TemplateExtractor {

    private String buildProperty(PropertyVO propertyVO, String name){

        StringBuffer text = new StringBuffer();
        text.append("<tr>");
        text.append("<td style=\"ord-break: keep-all;white-space:nowrap;\"\">%s</td>");
        text.append("<td style=\"ord-break: keep-all;white-space:nowrap;\"\">%s</td>");
        text.append("<td style=\"ord-break: keep-all;white-space:nowrap;\"\">%s</td>");
        text.append("<td style=\"ord-break: keep-all;white-space:nowrap;\"\">%s</td>");
        text.append("<td style=\"ord-break: keep-all;white-space:nowrap;\"\">%s</td>");
        text.append("</tr>");

        name = name+propertyVO.getName();
        String html = text.toString();
        String require = "";
        if(propertyVO.getRequire() != null){
            require = propertyVO.getRequire()?"是":"否";
        }
        String enumDesc = propertyVO.getRemark();
        html = String.format(html,name,propertyVO.getType(),propertyVO.getDesc(),require,enumDesc);

        return html;
    }

    public void buildPropertyContent(List<PropertyVO> propertyVOList, StringBuilder content, String name){

        String originalName = name;

        if(!CollectionUtils.isEmpty(propertyVOList)){
            for(PropertyVO propertyVO : propertyVOList){
                if(propertyVO.getFiledType() == PropertyVO.FiledType.BASIC_TYPE){
                    String html = buildProperty(propertyVO, name);
                    content.append(html);
                }else  if(propertyVO.getFiledType() == PropertyVO.FiledType.LIST){
                    String html = buildProperty(propertyVO, originalName);
                    content.append(html);
                    List<PropertyVO> subPropertyVOList = propertyVO.getPropertyVOList();
                    name = originalName+propertyVO.getName()+"[0].";
                    buildPropertyContent(subPropertyVOList, content, name);
                }else if(propertyVO.getFiledType() == PropertyVO.FiledType.SELF_DEFINED){
                    List<PropertyVO> subPropertyVOList = propertyVO.getPropertyVOList();
                    name =originalName+propertyVO.getName() + ".";
                    buildPropertyContent(subPropertyVOList, content, name);
                }
            }
        }
    }

    public String buildPropertyContent(List<PropertyVO> propertyVOList){
        StringBuilder content = new StringBuilder();
        buildPropertyContent(propertyVOList,content,"");
        return content.toString();
    }

    public String getTestInputItem(PropertyVO propertyVO,String value,String name){

        StringBuffer text = new StringBuffer();
        text.append("<div class=\"col-lg-4 mt-10\">");
        text.append("<div class=\"input-group\">");
        text.append("<span class=\"input-group-addon\" title=\"%s\">%s</span>");
        if(!CollectionUtils.isEmpty(propertyVO.getEnumInfoList())){
            text.append("<select type=\"text\" class=\"form-control %s\" name=\"%s\">");
            text.append("<option></option>");
            for (String str : propertyVO.getEnumInfoList()){
                String option = "<option value=\"%s\" %s>%s</option>";
                String key = str.split(":")[0];
                String desc = str.split(":")[1];
                String selected = "";

                if(!StringUtils.isEmpty(value) && value.equals(key)){
                    selected = "selected";
                }
                option = String.format(option,key,selected,desc+"("+key+")");
                text.append(option);
            }
            text.append("</select>");
        }else{
            text.append("<input type=\"text\" class=\"form-control %s\" name=\"%s\" value=\""+value+"\" aria-describedby=\"basic-addon1\">");
        }
        text.append("</div>");
        text.append("</div>");

        String desc= propertyVO.getDesc();
        if(StringUtils.isEmpty(desc)){
            desc = propertyVO.getName();
        }

        StringBuilder sb = new StringBuilder();
        Integer currentCount = 0;
        for (int i=0;i<desc.length();i++){
            String str = desc.substring(i,i+1);
            if(str.matches("^[\\u4e00-\\u9fa5]{1}$")){
                currentCount += 2;
            }else{
                currentCount +=1;
            }
            if(currentCount <= 16){
                sb.append(str);
            }
        }
        String shortDesc = sb.toString();
        String require = "";
        if(propertyVO.getRequire()){
            require = "require";
        }
        String html = text.toString();
        html = String.format(html,desc,shortDesc,require,name,value);
        return html;
    }

    public void buildTestContent(List<PropertyVO> propertyVOList, StringBuilder content, String name, ParamInfo paramInfo){

        String originalName = name;
        if(!CollectionUtils.isEmpty(propertyVOList)){
            for(PropertyVO propertyVO : propertyVOList){
                if(propertyVO.getFiledType() == PropertyVO.FiledType.BASIC_TYPE
                        || propertyVO.getFiledType() == PropertyVO.FiledType.SIMPLE_LIST){
                    name = originalName+propertyVO.getName();
                    String value = paramInfo.getParams().get(name);
                    if(value == null){
                        value = "";
                    }
                    String html = getTestInputItem(propertyVO,value,name);
                    content.append(html);
                }else if(propertyVO.getFiledType() == PropertyVO.FiledType.LIST){

                    List<PropertyVO> subList = propertyVO.getPropertyVOList();

                    content.append("<div class=\"clear mt-10 panel panel-default\">");
                    content.append("<div class=\"panel-heading\">"+propertyVO.getName()+"("+propertyVO.getDesc()+")");
                    content.append("<span class=\"glyphicon add glyphicon-plus\" ref=\""+propertyVO.getName()+"\" style=\"float:right;cursor:pointer\" aria-hidden=\"true\"></span>");
                    content.append("</div>");
                    content.append("<div class=\"panel-body\">");

                    Integer size = paramInfo.getListInfo().get(propertyVO.getName());

                    if(size == null){
                        size = 1;
                    }
                    for (int i=0;i<size;i++){
                        if(i == 0){
                            content.append("<div id=\""+propertyVO.getName()+"\">");
                        }
                        content.append("<div class=\"clear mt-10 panel panel-default\">");
                        content.append("<div class=\"panel-heading\">列表");
                        content.append("<span class=\"glyphicon minus glyphicon-minus\" " +
                                "style=\"float:right;cursor:pointer;display:"+(i==0?"none":"block")+";\" aria-hidden=\"true\"></span>");
                        content.append("</div>");
                        content.append("<div class=\"panel-body\">");


                        name = originalName+propertyVO.getName()+"["+i+"].";
                        buildTestContent(subList, content, name,paramInfo);
                        if(i == 0){
                            content.append("</div>");
                        }
                        content.append("</div>");
                        content.append("</div>");
                    }
                    content.append("</div>");
                    content.append("</div>");

                }else if(propertyVO.getFiledType() == PropertyVO.FiledType.SELF_DEFINED){
                    List<PropertyVO> subList = propertyVO.getPropertyVOList();
                    content.append("<div class=\"clear mt-10 panel panel-default\">");
                    content.append("<div class=\"panel-heading\">"+propertyVO.getName()+"("+propertyVO.getDesc()+")"+"</div>");
                    content.append("<div class=\"panel-body\">");

                    name =originalName+propertyVO.getName()+".";
                    buildTestContent(subList, content, name,paramInfo);
                    content.append("</div>");
                    content.append("</div>");
                }
            }
        }
    }

}
