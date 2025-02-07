package main

import (
	"fmt"
	"main/api/router"
	"main/config"
	db "main/db"
	"net/http"
	"os"
	"strconv"

	"github.com/gin-gonic/gin"
	eureka "github.com/xuanbo/eureka-client"
)

func init() {
	config.LoadEnvVariables()

	db.ConnectParsDB()
	db.GenerateParsDB()
}

func main() {

	port, err := strconv.Atoi(os.Getenv("PORT"))
	if err != nil {
		panic("Could not convert port to integer")
	}

	// Register with Eureka server
	client := eureka.NewClient(&eureka.Config{
		DefaultZone:           "http://127.0.0.1:8761/eureka/",
		App:                   "parsing",
		Port:                  port,
		RenewalIntervalInSecs: 10,
		DurationInSecs:        30,
		Metadata: map[string]interface{}{
			"VERSION":              "0.1.0",
			"NODE_GROUP_ID":        0,
			"PRODUCT_CODE":         "DEFAULT",
			"PRODUCT_VERSION_CODE": "DEFAULT",
			"PRODUCT_ENV_CODE":     "DEFAULT",
			"SERVICE_VERSION_CODE": "DEFAULT",
		},
	})

	client.Start()

	r := gin.Default()
	r.Use(func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")
		c.Writer.Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Accept, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization")
		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(http.StatusNoContent)
			return
		}
		c.Next()
	})

	router.GetRoute(r)
	r.Run(fmt.Sprintf(":%d", port))
}
