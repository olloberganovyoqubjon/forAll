package parsModels

import (
	"time"

	"gorm.io/gorm"
)

type Weather struct {
	gorm.Model
	TempMin           float32   `json:"tempMin"`
	TempMax           float32   `json:"tempMax"`
	WeatherNameFirst  string    `json:"weatherNameFirst"`
	WeatherNameSecond string    `json:"weatherNameSecond"`
	UpdateDate        time.Time `json:"updateDate" gorm:"type:timestamp"`
}
