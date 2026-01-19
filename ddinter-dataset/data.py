import pandas as pd
import glob

files = glob.glob("ddinter_downloads_code_*.csv")

dfs = [pd.read_csv(f) for f in files]
merged = pd.concat(dfs, ignore_index=True)

merged.to_csv("ddinter_merged.csv", index=False)

