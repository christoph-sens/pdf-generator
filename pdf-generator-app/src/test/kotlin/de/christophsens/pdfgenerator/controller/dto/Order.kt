package de.christophsens.pdfgenerator.controller.dto

data class Order(val items:List<Item>, val totalPrice: Double, val currency: String)
