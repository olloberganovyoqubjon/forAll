package parsModels

import "gorm.io/gorm"

type Sites struct {
	gorm.Model
	Name string `json:"name" gorm:"not null"`
}
