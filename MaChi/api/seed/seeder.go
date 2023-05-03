package seed

import (
	"log"

	"github.com/Jeshaiah04/MaChi/api/models"
	"github.com/jinzhu/gorm"
)

var users = []models.User{
	models.User{
		Nickname: "Jeshaiah",
		Email:    "jeshaiahjesse01@student.ciputra.ac.id",
		Password: "jeje123",
	},
	models.User{
		Nickname: "Jivni",
		Email:    "jivni@gmail.com",
		Password: "jivni123",
	},
}

var posts = []models.Post{
	models.Post{
		Title:   "Sekolah",
		Content: "Hello world 1",
	},
	models.Post{
		Title:   "Makanan",
		Content: "Hello world 2",
	},
}

func Load(db *gorm.DB) {

	err := db.Debug().DropTableIfExists(&models.Post{}, &models.User{}).Error
	if err != nil {
		log.Fatalf("cannot drop table: %v", err)
	}
	err = db.Debug().AutoMigrate(&models.User{}, &models.Post{}).Error
	if err != nil {
		log.Fatalf("cannot migrate table: %v", err)
	}

	err = db.Debug().Model(&models.Post{}).AddForeignKey("author_id", "users(id)", "cascade", "cascade").Error
	if err != nil {
		log.Fatalf("attaching foreign key error: %v", err)
	}

	for i, _ := range users {
		err = db.Debug().Model(&models.User{}).Create(&users[i]).Error
		if err != nil {
			log.Fatalf("cannot seed users table: %v", err)
		}
		posts[i].AuthorID = users[i].ID

		err = db.Debug().Model(&models.Post{}).Create(&posts[i]).Error
		if err != nil {
			log.Fatalf("cannot seed posts table: %v", err)
		}
	}
}
