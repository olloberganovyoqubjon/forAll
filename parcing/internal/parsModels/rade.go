package parsModels

import "gorm.io/gorm"

type Rade struct {
	gorm.Model
	Ccy  string  `json:"ccy"`
	Name string  `json:"name"`
	Rate float32 `json:"rate"`
	Dif  float32 `json:"dif"`
	Date string  `json:"date"`
}
