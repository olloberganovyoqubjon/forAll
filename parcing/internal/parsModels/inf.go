package parsModels

import (
	"time"

	"encoding/base64"
	_ "encoding/base64"

	"gorm.io/gorm"
)

type Inf struct {
	gorm.Model
	Link        string    `json:"link"  gorm:""`
	Title       string    `json:"title" gorm:""`
	Img         []byte    `json:"img" gorm:""`
	Create_date time.Time `json:"create_date" gorm:"type:timestamp"`
	Error       string    `json:"error" gorm:""`
	CategoryId  uint
}

func (i *Inf) GetBase64Img() string {
	return base64.StdEncoding.EncodeToString(i.Img)
}
