package parsModels

import "gorm.io/gorm"

type Rade struct {
	gorm.Model
	Date      string  `json:"date"`
	Code      string  `json:"code"`
	CbPrice   float32 `json:"cb_price"`
	Title     string  `json:"title"`
	ByPrice   float32 `json:"by_price"`
	CellPrice float32 `json:"cell_price"`
}
