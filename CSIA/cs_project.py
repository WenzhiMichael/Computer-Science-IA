import requests
from bs4 import BeautifulSoup
import csv

urls = [    'https://www.op.gg/champions?region=global&tier=platinum_plus&position=top',    'https://www.op.gg/champions?region=global&tier=platinum_plus&position=jungle',    'https://www.op.gg/champions?region=global&tier=platinum_plus&position=mid',    'https://www.op.gg/champions?region=global&tier=platinum_plus&position=adc',    'https://www.op.gg/champions?region=global&tier=platinum_plus&position=support']

positions = ['top', 'jungle', 'mid', 'adc', 'support']

for url, position in zip(urls, positions):
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36'
    }
    response = requests.get(url, headers=headers)

    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')
        table = soup.find_all('table')[0]
        rows = table.find_all('tr')[1:]

        data = []
        for row in rows:
            columns = row.find_all('td')
            champion = columns[1].find('a').get_text().strip()
            tier = columns[2].get_text().strip()
            win_rate = columns[3].get_text().strip()
            pick_rate = columns[4].get_text().strip()
            ban_rate = columns[5].get_text().strip()
            weak_against = [champ.get('alt').strip() for champ in columns[6].find_all('img')]
            data.append([champion, tier, win_rate, pick_rate, ban_rate, ";".join(weak_against)])

        # Sort the data by Tier (ascending) and then by Win Rate (descending)
        data = sorted(data, key=lambda x: (x[1], -float(x[2].replace('%', ''))))

        # Write the data to a CSV file
        filename = position + '.csv'
        with open(filename, 'w', newline='') as file:
            writer = csv.writer(file)
            writer.writerow(['Champion', 'Tier', 'Win Rate', 'Pick Rate', 'Ban Rate', 'Weak Against'])
            writer.writerows(data)

        print(f"Data saved to {filename}")

    else:
        print(f"Response error: {response.status_code}")
