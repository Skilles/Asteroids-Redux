# Asteroids
Asteroids Redux is my attempt at creating a modern version of the 1979 arcade game "Asteroids" in Java using JavaFX. Shoot and dodge the asteroids to survive and gain
score. Asteroids will split into pieces, with smaller pieces being worth more points.
Occasionally, you will encounter a large saucer that shoots randomly as well as a small saucer that shoots at you
and gets increasingly more accurate as your score gets higher. Press the Shift button to go into hyperspace, which
teleports you to a new random location, but be careful, as you may teleport into a more dangerous situation! You gain 1
health every 5000 points, and you lose 1 health every time you hit an asteroid or a saucer's bullet. The game gets more
difficult as your score increases and continues until you run out of health.

### Features
- Original enemies with new sprites
- Hyperspace warp
- Custom physics-based collisions
- Screen wraparound
- Scoring system
- Custom sounds
  

### Scoring
##### Asteroids
> Tiny: 100 points \
> Small: 50 points \
> Medium: 20 points \
> Large: 10 points

##### Saucers
> Small: 1000 points \
> Large: 200 points

### What I learned
- General game design principles
- Using vector math to calculate movement and collisions
- Using 2D kinematics in a game
- Using JavaFX and its canvas class to create a multi-scened game
- How to make simple sounds using jsfxr
- How to use maven

### Retrospect
If I were to start over, I would plan out my project first instead of learning and restructuring as I go. I would also structure my project in a
way that would allow me to easily add more features such as a high-score system and weapon power-ups. Lastly, if this
was a project that I would want to release publicly, I would use more self-authored assets.