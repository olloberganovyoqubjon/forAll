package router

import (
	"main/api/controllers"

	"github.com/gin-gonic/gin"
)

func GetRoute(r *gin.Engine) {
	//yangi pars.json file ni uploud qiladi
	r.POST("/uploadPars", controllers.UploadPars)
	//hammma sayt va kategoruyalarini pars qiladi
	r.GET("/allSitesWithCategory", controllers.GetAllSitesWithCategory)
	//oxirgi 10 ta prezident yangiliklarini olib beradi
	r.GET("/presidentNews", controllers.GetPresidentNews)
	//sayt siteId ga qarab yangiliklarni olib beradi
	r.GET("/newsByIdSite/:id", controllers.GetNewsByIdSite)
	//ob-havo olib beradi
	r.GET("/wether", controllers.GetWether)
	//valyutalarni bazadan oladi
	r.GET("/rade", controllers.Getrade)
	//sayt siteId  va categoryId ga qarab yangiliklarni olib beradi
	//(siteId va categoryId 0 bolsa hamma oxirgi yangiliklarno olib beradi),
	//(categoryId 0 bolsa siteId ga qarab oxirgi yangiliklarno olib beradi)
	r.GET("/newsByIdSites/:idSite/newsByIdCategory/:idCategory", controllers.GetNewsByIdSitesAndIdCategory)
}
