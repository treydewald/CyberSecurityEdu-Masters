package com.example.cybersecurityeducation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserTypeViewModel : ViewModel(){

    var userType2= "not changed"

    //var userType = MutableLiveData<String>()

    fun getType() : String{
        return userType2
    }

    fun setType(param : String){
        userType2 = param
    }


}