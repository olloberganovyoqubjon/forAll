package parsModels

import "gorm.io/gorm"

type Category struct {
	gorm.Model
	Name         string `json:"name" gorm:"not null"`
	CategoryName string `json:"categoryName"`
	SiteId       uint
}
