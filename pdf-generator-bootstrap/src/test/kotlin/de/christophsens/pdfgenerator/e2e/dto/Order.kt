package de.christophsens.pdfgenerator.e2e.dto

data class Order(val items:List<Item>, val totalPrice: Double, val currency: String)
