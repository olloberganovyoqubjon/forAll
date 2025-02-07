package parsModels

import "gorm.io/gorm"

type Contents struct {
	gorm.Model `json:"gorm.Model"`
	Content    string `json:"content" gorm:"" json:"content,omitempty"`
	InfID      uint
}
