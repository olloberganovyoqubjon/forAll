package controllers

import (
	db "main/db"
	"main/internal/parsModels"
	"net/http"

	"github.com/gin-gonic/gin"
)

func GetRades(c *gin.Context) {
	var radeUSDModel parsModels.Rade
	var radeEURModel parsModels.Rade
	var radeRUBModel parsModels.Rade
	db.ParsDB.First(&radeUSDModel, "code = ?", "USD").Order("id desc")
	db.ParsDB.First(&radeEURModel, "code = ?", "EUR").Order("id desc")
	db.ParsDB.First(&radeRUBModel, "code = ?", "RUB").Order("id desc")
	var radeModels []parsModels.Rade
	radeModels = append(radeModels, radeUSDModel, radeEURModel, radeRUBModel)

	c.JSON(http.StatusOK, gin.H{
		"allRades": radeModels,
	})

}
