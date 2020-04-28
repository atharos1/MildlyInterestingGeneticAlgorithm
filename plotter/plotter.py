import matplotlib.pyplot as plt

with open('data.dat', 'r') as f:
    y = f.read().splitlines()
    #x = [item for item in range(0, len(y))] 

    plt.plot(y)
    plt.ylabel('Fitness')
    plt.xlabel('Iteración')
    plt.show()