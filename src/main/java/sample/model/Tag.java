package sample.model;

public class Tag {
    private String name;
    private String value;


    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getString() {
        String allTag = "<" + name + ">";
        if(value!=""){
            allTag += value;
        }
        else{
            allTag+="\n";
        }


        return allTag;
    }
}
