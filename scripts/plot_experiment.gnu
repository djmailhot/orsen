set terminal png
set output "orsen_chart.png"
set autoscale

set xlabel "Gold Standard Entity's Computed Rank k"
set ylabel "Quantity of Samples"
set title "Quality of Gold Standard Entity Computed Ranks"
# set key bottom right
set key top right

# Visuals
set style line 80 lt rgb "#808080"
set style line 81 lt 0
set grid back linestyle 81

set style fill solid
set boxwidth 0.5

# Line Colors
set style line 1 lt rgb "#A00000" lw 2 pt 1
set style line 2 lt rgb "#00A000" lw 2 pt 1
set style line 3 lt rgb "#0000A0" lw 2 pt 1

plot 'data/orsen_bad_experiment_table.txt' using 1:3:xtic(2) with boxes title ''
