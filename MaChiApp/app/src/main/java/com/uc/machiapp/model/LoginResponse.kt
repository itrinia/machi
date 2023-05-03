package com.uc.machiapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(val token: String)

//"status":   "success",
//"token":    tokenString,
//"id":       user.ID,
//"nickname": user.Nickname,
//"email":    user.Email,
//"password": user.Password,