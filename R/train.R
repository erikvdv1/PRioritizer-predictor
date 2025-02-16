#!/bin/Rscript

### Install packages
source("helper/install-packages.R")

### Include helpers
source("algorithms/random-forest.R")
source("helper/data.R")
source("helper/model.R")

# ================================== PROGRAM ================================== #

args <- commandArgs(TRUE)

if (length(args) != 1) {
  stop("Expected one argument.\nUsage: Rscript train.R <dir>")
}

### Files
dir <- args[1]
input.file <- file.path(dir, "training.csv")
output.file <- file.path(dir, "model.RData")

### Read data
data <- read.data(input.file)

### Train model
trained.model <- random.forest.train(model, data)
save(trained.model, file = output.file)
