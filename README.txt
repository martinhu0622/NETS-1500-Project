# 🎶 Global Echo: Virtual Music Festival Map

## 📝 Overview

**Global Echo** is a personalized recommendation tool that helps music festival attendees create an optimal itinerary. With overlapping performances and multiple stages, planning can be overwhelming. This tool streamlines the process by:

* Maximizing the number of performances a user can attend
* Minimizing walking time between stages
* Avoiding scheduling conflicts

## 🛠️ Technologies & Approach

* **Concept:** Graph theory and pathfinding algorithms (Dijkstra’s)
* **Implementation:**

  * Each artist’s performance is a **node**
  * Paths between stages are **edges**
  * **Edge weights** are calculated based on:

    * Walking time between stages
    * Performance time overlap/conflicts
    * User preferences

## 👥 Team Contributions

* **Ryka** – Wrote the project description and created a sample dataset of artists, genres, and coordinates
* **Bowen** – Developed the backend algorithm for itinerary optimization
* **Martin** – Built and styled the frontend user interface for an engaging experience
