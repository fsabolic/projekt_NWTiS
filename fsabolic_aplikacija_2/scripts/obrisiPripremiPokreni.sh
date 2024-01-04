#!/bin/bash
docker stop fsabolic_payara_micro
echo "Zaustavljena slika za payaru micro"
sleep 2
docker rm fsabolic_payara_micro
echo "Obrisana slika za payaru micro"
./scripts/pripremiSliku.sh
echo "Pripremljena slika za payaru micro"
./scripts/pokreniSliku.sh
echo "Pokrenuta slika za payaru micro"
