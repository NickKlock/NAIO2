package core.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Bd{
    private  String id;
    private LocalDate bd;
    private String bdString;


    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public LocalDate getBd(){
        return bd;
    }

    public void setBd(LocalDate bd){
        this.bdString = bd.format(DateTimeFormatter.ofPattern("dd.MM.yyyy",Locale.GERMAN));
        this.bd = bd;
    }
}
