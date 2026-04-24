import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix


df = pd.read_csv("Sample.csv")

df.replace([np.inf, -np.inf], np.nan, inplace=True)
df.fillna(0, inplace=True)
df.drop(columns=["SchedType", "CoreCount", "CoreId", "IsCoreIdle"], inplace=True)

X = df.drop("Action", axis=1)
y = df["Action"]

print("Rows:", len(df))
print("\nAction counts overall:\n", df["Action"].value_counts())
print("\nNumber of features:", len(df.columns) - 1)
print("Feature names:", X.columns.tolist())

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
model = RandomForestClassifier(n_estimators=1000, random_state=42, n_jobs=-1)
model.fit(X_train, y_train)

y_pred = model.predict(X_test)

print("\nAccuracy:", round(accuracy_score(y_test, y_pred), 4))
print("\nClassification Report:")
print(classification_report(y_test, y_pred))
print("Confusion Matrix (rows=true, cols=pred):")
print(confusion_matrix(y_test, y_pred))

print("\n" + "=" * 70)
print("SIMPLE SIMULATION (ML predicts next queue-level action)")
print("=" * 70)

n = int(input("How many processes? (min 2): "))
while n < 2:
    n = int(input("Must be >=2. Enter again: "))

quantum = int(input("RR quantum (e.g., 2): "))

processes = []
total_burst = 0

for i in range(n):
    arrival = int(input(f"P{i} arrival time: "))
    burst = int(input(f"P{i} burst time: "))
    processes.append({"pid": f"P{i}", "arrival": arrival, "remaining": burst})
    total_burst += burst


time = min(p["arrival"] for p in processes)
ready_queue = []
completed = []
current_process = None
bursts_done = 0
rr_counter = 0
log = []

print("\n" + "=" * 70)
print("SCHEDULER DECISION LOG")
print("=" * 70)

while len(completed) < n:

    for p in processes:
        if p["arrival"] == time:
            ready_queue.append(p)

    ready_queue = [p for p in ready_queue if p["remaining"] > 0]
    remaining_times = [p["remaining"] for p in ready_queue] or [0]

    feature_df = pd.DataFrame(0.0, index=[0], columns=X.columns)
    feature_df["QueueThreadCount"] = len(ready_queue)
    feature_df["QueueMinRemainingBursts"] = min(remaining_times)
    feature_df["QueueMaxRemainingBursts"] = max(remaining_times)
    feature_df["QueueMeanRemainingBursts"] = np.mean(remaining_times)
    feature_df["QueueTotalRemainingBursts"] = sum(remaining_times)
    feature_df["ThreadBurstsRan"] = bursts_done
    feature_df["ThreadBurstsRemaining"] = total_burst - bursts_done


    action = model.predict(feature_df)[0]


    if action == "NEXT_OTHER":
        action = "NEXT_FCFS"

    if action == "NONE" and current_process is None:
        action = "NEXT_FCFS"

    chosen_pid = "IDLE"
    remaining_after = None

    if len(ready_queue) == 0:
       log.append({"Time": time, "ReadyQueue": [], "Action": "IDLE", "Chosen": "IDLE", "RemainingAfter": None})
       time += 1
       continue

    if action == "NEXT_FCFS":
        current_process = sorted(ready_queue, key=lambda x: x["arrival"])[0]
        rr_counter = 0
    elif action == "NEXT_SJF":
        current_process = min(ready_queue, key=lambda x: x["remaining"])
        rr_counter = 0
    elif action == "NEXT_RR":
        if current_process is None or rr_counter >= quantum:
            if current_process in ready_queue:
                ready_queue.append(ready_queue.pop(0))
            current_process = ready_queue[0]
            rr_counter = 0
    elif action == "NONE":
        pass

    if current_process:
        current_process["remaining"] -= 1
        bursts_done += 1
        rr_counter += 1

        chosen_pid = current_process["pid"]
        remaining_after = current_process["remaining"]

        if current_process["remaining"] == 0:
            completed.append(current_process)
            ready_queue.remove(current_process)
            current_process = None
            rr_counter = 0

    queue_pids = [ p["pid"] for p in ready_queue if current_process is None or p != current_process]
    
    log.append({
    "Time": time,
    "ReadyQueue": queue_pids,
    "Action": action,
    "Chosen": chosen_pid,
    "RemainingAfter": remaining_after})
    time += 1

log_df = pd.DataFrame(log)
print(log_df)

