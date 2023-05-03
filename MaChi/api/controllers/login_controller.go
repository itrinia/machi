package controllers

import (
	"encoding/json"
	"github.com/Jeshaiah04/MaChi/api/responses"
	"github.com/Jeshaiah04/MaChi/api/utils/formaterror"
	"github.com/dgrijalva/jwt-go"
	"io/ioutil"
	"net/http"
	"os"
	"time"

	"github.com/Jeshaiah04/MaChi/api/models"
	"golang.org/x/crypto/bcrypt"
)

func (server *Server) Login(w http.ResponseWriter, r *http.Request) {
	// Read the request body and unmarshal it into a models.User struct
	body, err := ioutil.ReadAll(r.Body)
	if err != nil {
		responses.ERROR(w, http.StatusUnprocessableEntity, err)
		return
	}
	user := models.User{}
	err = json.Unmarshal(body, &user)
	if err != nil {
		responses.ERROR(w, http.StatusUnprocessableEntity, err)
		return
	}

	// Validate the login credentials
	user.Prepare()
	err = user.Validate("login")
	if err != nil {
		responses.ERROR(w, http.StatusUnprocessableEntity, err)
		return
	}

	// Generate the JWT
	token, err := server.SignIn(user.Email, user.Password)
	if err != nil {
		formattedError := formaterror.FormatError(err.Error())
		responses.ERROR(w, http.StatusUnprocessableEntity, formattedError)
		return
	}

	// Return the JWT in the response
	responses.JSON(w, http.StatusOK, token)
}

//func (server *Server) Login(w http.ResponseWriter, r *http.Request) {
//	body, err := ioutil.ReadAll(r.Body)
//	if err != nil {
//		responses.ERROR(w, http.StatusUnprocessableEntity, err)
//		return
//	}
//	user := models.User{}
//	err = json.Unmarshal(body, &user)
//	if err != nil {
//		responses.ERROR(w, http.StatusUnprocessableEntity, err)
//		return
//	}
//
//	user.Prepare()
//	err = user.Validate("login")
//	if err != nil {
//		responses.ERROR(w, http.StatusUnprocessableEntity, err)
//		return
//	}
//	token, err := server.SignIn(user.Email, user.Password)
//	if err != nil {
//		formattedError := formaterror.FormatError(err.Error())
//		responses.ERROR(w, http.StatusUnprocessableEntity, formattedError)
//		return
//	}
//	responses.JSON(w, http.StatusOK, token)
//}

func (server *Server) SignIn(email, password string) (map[string]interface{}, error) {

	var err error

	user := models.User{}

	err = server.DB.Debug().Model(models.User{}).Where("email = ?", email).Take(&user).Error
	if err != nil {
		return nil, err
	}
	err = models.VerifyPassword(user.Password, password)
	if err != nil && err == bcrypt.ErrMismatchedHashAndPassword {
		return nil, err
	}

	// Generate the JWT
	token := jwt.NewWithClaims(jwt.GetSigningMethod("HS256"), jwt.MapClaims{
		"user_id": user.ID,
		"exp":     time.Now().Add(time.Hour * 1).Unix(),
	})
	tokenString, _ := token.SignedString([]byte(os.Getenv("API_SECRET")))

	// Return the response
	return map[string]interface{}{
		"status":   "success",
		"token":    tokenString,
		"id":       user.ID,
		"nickname": user.Nickname,
		"email":    user.Email,
		"password": user.Password,
	}, nil
}
