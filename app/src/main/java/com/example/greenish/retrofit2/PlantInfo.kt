package com.example.greenish.retrofit2

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class PlantInfo(
    @Element(name = "body")
    val body: Body,
    @Element(name="header")
    val header: Header
)

@Xml(name="header")
data class Header(
    @PropertyElement(name="resultCode")
    val resultCode: Int,
    @PropertyElement(name="resultMsg")
    val resultMsg: String
)

@Xml(name = "body")
data class Body(
    @Element(name="item")
    val item: List<Item>
)

@Xml
data class Item(
    @PropertyElement(name = "speclmanageInfo")
    var speclmanageInfo: String,
    @PropertyElement(name = "postngplaceCodeNm")
    var postngplaceCodeNm: String,
    @PropertyElement(name = "prpgtEraInfo")
    var prpgtEraInfo: String,
    @PropertyElement(name = "soilInfo")
    var soilInfo: String,
    @PropertyElement(name = "watercycleAutumnCodeNm")
    var watercycleAutumnCodeNm: String
)