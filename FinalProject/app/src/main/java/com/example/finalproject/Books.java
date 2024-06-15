package com.example.finalproject;

import java.util.Date;

//bu classı gereken verilerimi bir tür üzerinden, bir arada almak için oluşturdum.
public class Books {

    int id;
    String Name;
    String Writer;
    String Readdate;

    public Books(int id, String Name, String Writer, String Readdate){
        this.id = id;
        this.Name = Name;
        this.Writer = Writer;
        this.Readdate = Readdate;
    }
}
