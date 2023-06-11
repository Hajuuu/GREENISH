package com.example.greenish.retrofit

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class PlantResult(
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
    @Element(name="items")
    val items: Items
)

@Xml(name= "items")
data class Items(
    @Element(name="item")
    val item: List<Item>
)


@Xml
data class Item(
    @PropertyElement(name = "cntntsNo")
    var cntntsNo: String,
    @PropertyElement(name = "cntntsSj")
    var cntntsSj: String
)