cd out/production/II.3502-lab3-fault-tolerance-java

start cmd /k java FT.FTBillboardServer localhost:1099
timeout 0.5
start cmd /k java FT.FTBillboardServer localhost:1250 localhost:1099

timeout 1
start cmd /k java FT.ClientMain localhost:1099