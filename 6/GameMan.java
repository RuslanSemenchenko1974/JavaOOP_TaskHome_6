/**
 
Разобраться с проектом. Подготовить вопросы к следующему семинару, если возникнут.
Добавить валидацию параметров при создании карты
Реализовать возможность задать ограничение по количеству создаваемых на карте роботов (в конструкторе карты). 
При этом если параметр не указан, то используем значение по-умолчанию: 5
Реализовать возможность вызова метода move с параметром - количество шагов вперед. Подсказка: можно несколько раз 
вызвать метод #move


 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;


public class GameMan {

    public static void main(String[] args) {
        System.out.print("\033\143");
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите размеры карты ");
        int n = sc.nextInt();
        int m = sc.nextInt();
        sc.nextLine();

        final RobotMap map = new RobotMap(n, m);
       
       System.out.println(" Карта создана ");

       CommandManager manager = new CommandManager(map);
       while(true) {
       System.out.println("""
               Доступные действия:
            1. Длясоздания робота введите команду create x y
            2. Для вывода списка всех роботов введите list
            3. Для перемещения робота введите move id
            4. Для изменения направления введите changedir id DIRECTION
            5. Для удаления робота введите delete id
            6. Для выхода exit
               """);

        String command = sc.nextLine();
        manager.acceptCommand(command);


        
    }
        
    }

    private static class CommandManager{
        private final List <CommandHandler> handlers;
        private final RobotMap map;
        public CommandManager(RobotMap map){
            this.map = map;
            handlers = new ArrayList<>();
            initHandlers();
        }
        private void initHandlers(){
            initCreateCommandHandlers();
            initListCommandHandlers();
            initMoveCommandHandlers();
            initExitCommandHandlers();
            initChangeDirCommandHandlers();
            initDeleteCommandHandlers();
        }
        private void initCreateCommandHandlers(){
            handlers.add(new CommandHandler(){

                @Override
                public String name() {
                    return "create";
                }

                @Override
                public void runCommand(String[] args) {
                    int x = Integer.parseInt(args[0]);
                    int y = Integer.parseInt(args[1]);
                    //Создаем робота на позиции  x,y
                    RobotInt robot = map.createRobot(new Point(x,y));
                    System.out.println("Робот "+robot+" успешно создан");
                }
        });
    }
        private void initListCommandHandlers(){
            handlers.add(new CommandHandler(){

                @Override
                public String name() {
                    return "list";
                }

                @Override
                public void runCommand(String[] args) {
                    map.acceptRobots(System.out::println);
                }
        });
       
        }

        private void initMoveCommandHandlers(){
            handlers.add(new CommandHandler(){

                @Override
                public String name() {
                    return "move";
                }

                @Override
                public void runCommand(String[] args) {
                    Long robotId = Long.parseLong(args[0]);
                    Optional <RobotInt> robot = map.getById(robotId);
                    robot.ifPresentOrElse(new Consumer<RobotInt>() {

                        @Override
                        public void accept(RobotInt robot) {
                           robot.move(1);
                        }
                        
                    }, new Runnable() {
            
                        @Override
                        public void run() {
                            System.out.println("Робот "+robotId+" на найден!");
                        }
                        
                    });
                }
        });
       
        }

        private void initExitCommandHandlers(){
            handlers.add(new CommandHandler(){

                @Override
                public String name() {
                    return "exit";
                }

                @Override
                public void runCommand(String[] args) {
                    System.exit(0);
                }
        });
       
        }

        private void initChangeDirCommandHandlers(){
            handlers.add(new CommandHandler(){

                @Override
                public String name() {
                    return "changedir";
                }

                @Override
                public void runCommand(String[] args) {
                    Long robotId = Long.parseLong(args[0]);
                    Optional <RobotInt> robot = map.getById(robotId);
                    robot.ifPresentOrElse(new Consumer<RobotInt>() {

                        @Override
                        public void accept(RobotInt robot) {
                            robot.changeDirection(Direction.ofString(args[1]));
                            System.out.println("Робот "+robotId+" изменил направление на "+args[1]);
                            //System.out.println(robot);
                        }
                        
                    }, new Runnable() {
            
                        @Override
                        public void run() {
                            System.out.println("Робот "+robotId+" на найден!");
                        }
                        
                    });
                }
        });
       
        }

        private void initDeleteCommandHandlers(){
            handlers.add(new CommandHandler(){

                @Override
                public String name() {
                    return "delete";
                }

                @Override
                public void runCommand(String[] args) {
                    Long robotId = Long.parseLong(args[0]);
                    Optional <RobotInt> robot = map.getById(robotId);
                    robot.ifPresentOrElse(new Consumer<RobotInt>() {

                        @Override
                        public void accept(RobotInt robot) {
                            map.deleteRobot(robotId);
                            System.out.println("Робот "+robotId+" удален");
                            //System.out.println(robot);
                        }
                        
                    }, new Runnable() {
            
                        @Override
                        public void run() {
                            System.out.println("Робот "+robotId+" на найден!");
                        }
                        
                    });
                }
        });
       
        }



        public void acceptCommand(String command){

        String[] split = command.split(" ");
        String commandName = split[0];
        String[] commandArgs = Arrays.copyOfRange(split, 1, split.length);

        boolean found = false;
        for (CommandHandler handler:handlers){
       
        if (commandName.equals(handler.name())) {
            found = true;
            try {
                handler.runCommand(commandArgs);
            }catch (Exception e){
                System.err.println("Во время обработки команды " + commandName+" произошла ошибка: "+e.getMessage());
            }
        }
    }   
    if (!found) {System.out.println("Команда не найдена");}
    
        }
    }
    private interface CommandHandler{
        String name();
        void runCommand(String[] args);
    }

}