package initializers

import (
	"fmt"
	"log"
	"main/internal/parsModels"
	"os"

	"gorm.io/gorm/logger"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var ParsDB *gorm.DB

func ConnectParsDB() {
	var err error
	dns := os.Getenv("DNSPars")
	ParsDB, err = gorm.Open(postgres.Open(dns), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info),
	})

	if err != nil {
		log.Fatal("Database connection failed!")
	}
}

func GenerateParsDB() {
	errm := ParsDB.AutoMigrate(parsModels.Sites{}, parsModels.Inf{}, parsModels.Contents{}, parsModels.Category{}, parsModels.Rade{})
	if errm != nil {
		log.Fatal("Migration failed")
	}
	fmt.Println("Migration success")
}
