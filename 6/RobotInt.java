public interface RobotInt {
    
        public Long getID();
        public void changeDirection(Direction direction);
        public void move(int step);
        public Point getPoint();

    }

