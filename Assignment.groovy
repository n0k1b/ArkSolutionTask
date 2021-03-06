// ---------------------------
//
// HOMEWORK
//
// You can use either Groovy or Java.
//
// Design a routine that will calculate the average Product price per Group.
//
// The Price of each Product is calculated as:
// Cost * (1 + Margin)
//
// Assume there can be a large number of products and a large number of categories.
//

// Plus points:
// - use Groovy and its closures
// - make the category look-up performance effective
// - use method Collection.inject

// contains information about [Product, Group, Cost]

def products = [
  ["A", "G1", 20.1],
  ["B", "G2", 98.4],
  ["C", "G1", 49.7],
  ["D", "G3", 35.8],
  ["E", "G3", 105.5],
  ["F", "G1", 55.2],
  ["G", "G1", 12.7],
  ["H", "G3", 88.6],
  ["I", "G1", 5.2],
  ["J", "G2", 72.4]
]

// contains information about Category classification based on product Cost
// [Category, Cost range from (inclusive), Cost range to (exclusive)]
// i.e. if a Product has Cost between 0 and 25, it belongs to category C1
def categories = [
  ["C3", 50, 75],
  ["C4", 75, 100],
  ["C2", 25, 50],
  ["C5", 100, null],
  ["C1", 0, 25]
]

// contains information about margins for each product Category
// [Category, Margin (either percentage or absolute value)]

def margins = [
  "C1": "20%",
  "C2": "30%",
  "C3": "0.4",
  "C4": "50%",
  "C5": "0.6"
]

//Format Margins Data With Fixed Format
def formatedMargins = margins.inject([: ]) {
  result,
  margin ->
  if (margin.value.endsWith('%'))
    result[margin.key] = margin.value.minus('%').toInteger() / 100
  else
    result[margin.key] = margin.value.toFloat()
  result
}

//Convert Category Data to TreeMap For Search Category With LOG(N) Time Complexity
def categoryMap = [: ] as TreeMap
categories.each {
  category ->
    categoryMap.put(category[1] as BigDecimal, category[0])
}

//Find Product Category By Their Price
def findCategory = {
  productPrice -> ((String) categoryMap.floorEntry(productPrice as BigDecimal)).split('=')[1]
}

//Calculate Product Price With Margin Value
def calculateProductPriceWithMargin = {
  productPrice -> productPrice.multiply((1. plus(formatedMargins[findCategory(productPrice)])))
}

//Find Porduct Price With Margin By Group
def groupProductPriceWithMargin = products.inject(['G1': [], 'G2': [], 'G3': []]) {
  result,
  product ->
  result[product[1]].push(calculateProductPriceWithMargin(product[2]))
  result
}

//Find Average Product Price By Group
def avgGroupProductPrice = groupProductPriceWithMargin.inject([: ]) {
  result,
  price ->
  result[price.key] = (price.value.sum() / price.value.size()).round(1)
  result
}

def result = avgGroupProductPrice

// ---------------------------
//
// IF YOUR CODE WORKS, YOU SHOULD GET "It works!" WRITTEN IN THE CONSOLE
//
// ---------------------------
assert result == [
  "G1": 37.5,
  "G2": 124.5,
  "G3": 116.1
]: "It doesn't work"

println "It works!"
