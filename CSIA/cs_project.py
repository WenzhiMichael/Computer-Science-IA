import requests
from bs4 import BeautifulSoup
import csv

url = 'https://www.op.gg/champions?region=global&tier=platinum_plus&position=top'
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36'}
response = requests.get(url, headers=headers)

if response.status_code == 200:
    soup = BeautifulSoup(response.text, 'html.parser')
    table = soup.find_all('table')[0]
    rows = table.find_all('tr')[1:]

    data = []
    for row in rows:
        columns = row.find_all('td')
        rank = columns[0].get_text().strip()
        champion = columns[1].find('a').get_text().strip()
        tier = columns[2].get_text().strip()
        win_rate = columns[3].get_text().strip()
        pick_rate = columns[4].get_text().strip()
        ban_rate = columns[5].get_text().strip()
        weak_against = [champ.get('alt').strip() for champ in columns[6].find_all('img')]
        data.append([rank, champion, tier, win_rate, pick_rate, ban_rate, ", ".join(weak_against)])

    # Sort the data by Tier (ascending) and then by Win Rate (descending)
    data = sorted(data, key=lambda x: (x[2], -float(x[3].replace('%', ''))))

    # Write the data to a CSV file
    with open('top.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Rank', 'Champion', 'Tier', 'Win Rate', 'Pick Rate', 'Ban Rate', 'Weak Against'])
        writer.writerows(data)

    print("Data saved to top.csv")

else:
    print(f"Response error: {response.status_code}")
