#!/bin/bash
docker stop nwtishsqldb_2
echo "Zaustavljena slika za bazu"
sleep 2
docker rm nwtishsqldb_2
echo "Obrisana slika za bazu"
./scripts/pripremiSliku.sh
echo "Pripremljena slika za bazu"
./scripts/pokreniSliku.sh
echo "Pokrenuta slika za bazu"

