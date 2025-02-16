### Install packages
source("helper/install-packages.R")

### Include helpers
source("algorithms/all.R")
source("helper/data.R")
source("helper/evaluation.R")
source("helper/model.R")
source("helper/utils.R")

# Select algorithms
select <- list(
  logistic.regression = FALSE,
  naive.bayes = FALSE,
  random.forest = TRUE,
  support.vector.machine = FALSE
)

# ================================== PROGRAM ================================== #

### Read data
data <- read.data("test/csv/angular.csv")

### Single run
data <- split.data(data, .90)
results <- models.evaluate(model, data$train, data$test, select)
print(results)
