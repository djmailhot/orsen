# terminal png
set output "/tmp/orsen_experiment_output/plot.png"
# plot /tmp/orsen_experiment_output/data.csv
plot 'example_data.dat' using 1:1 title 'A', \
     'example_data.dat' using 1:2 title 'B'
