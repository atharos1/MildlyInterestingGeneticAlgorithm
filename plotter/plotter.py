import matplotlib.pyplot as plt

with open('data.dat', 'r') as f:
    y = [float(line) for line in f.read().splitlines()]

    plt.plot(y, 'ro')
    plt.ylabel('Fitness')
    plt.xlabel('Iteración')
    plt.show()
