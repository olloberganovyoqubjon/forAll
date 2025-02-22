package main

import (
	"fmt"
	"main/api/router"
	"main/config"
	db "main/db"
	"net"
	"os"

	"github.com/gin-gonic/gin"
	eureka "github.com/xuanbo/eureka-client"
)

func init() {
	config.LoadEnvVariables()

	db.ConnectParsDB()
	db.GenerateParsDB()
}

func main() {

	// port, err := strconv.Atoi(os.Getenv("PORT"))
	// if err != nil {
	// 	panic("Could not convert port to integer")
	// }

	// Tasodifiy bo'sh port olish
	listener, err := net.Listen("tcp", ":0")
	if err != nil {
		panic("Could not get a random port")
	}
	port := listener.Addr().(*net.TCPAddr).Port
	listener.Close() // Portni bo'shatamiz

	// Register with Eureka server
	client := eureka.NewClient(&eureka.Config{
		DefaultZone:           os.Getenv("EUREKA_SERVER_URL"),
		App:                   os.Getenv("APP_NAME"),
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
	router.GetRoute(r)
	// r.Run(fmt.Sprintf(":%d", port))
	r.Run(fmt.Sprintf(":%d", port))
}
