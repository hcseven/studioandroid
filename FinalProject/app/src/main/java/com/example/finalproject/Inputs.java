package com.example.finalproject;

import kotlinx.coroutines.channels.ActorKt;

//bu classı gereken verilerimi bir tür üzerinden, bir arada almak için oluşturdum.
public class Inputs {

    int id;
    String Title;
    String Input;

    int kitapid;
    int inputtype;

    public Inputs(int id, String Title, String Input, int kitapid, int inputtype)
    {
        this.id = id;
        this.Title = Title;
        this.Input = Input;
        this.kitapid = kitapid;
        this.inputtype = inputtype;
    }


}
