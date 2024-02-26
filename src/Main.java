import java.util.*;

// Định nghĩa một công việc
class Job {
    String name;
    int hours;

    public Job(String name, int hours) {
        this.name = name;
        this.hours = hours;
    }
}

// Lớp biểu diễn lịch trình làm việc của một nhân viên trong một ngày
class DailySchedule {
    List<Job> jobs;
    int remainingHours;

    public DailySchedule(int remainingHours) {
        this.jobs = new ArrayList<>();
        this.remainingHours = remainingHours;
    }

    // Thêm một công việc vào lịch trình ngày làm việc
    public void addJob(Job job) {
        jobs.add(job);
        remainingHours -= job.hours;
    }

    // Kiểm tra xem lịch trình ngày làm việc có còn thời gian trống không
    public boolean hasRemainingHours() {
        return remainingHours > 0;
    }
}

// Lớp biểu diễn lịch trình làm việc của một nhân viên trong cả tháng
class MonthlySchedule {
    Map<Integer, DailySchedule> scheduleMap;

    public MonthlySchedule(int daysInMonth, int hoursPerDay) {
        scheduleMap = new HashMap<>();
        for (int i = 1; i <= daysInMonth; i++) {
            scheduleMap.put(i, new DailySchedule(hoursPerDay));
        }
    }

    // Thêm một công việc vào lịch trình làm việc trong tháng
    public void addJob(int day, Job job) {
        DailySchedule dailySchedule = scheduleMap.get(day);
        dailySchedule.addJob(job);
    }

    // Kiểm tra xem lịch trình làm việc trong tháng có còn ngày nào chưa sắp xếp công việc không
    public boolean hasRemainingDays() {
        for (DailySchedule dailySchedule : scheduleMap.values()) {
            if (dailySchedule.hasRemainingHours()) {
                return true;
            }
        }
        return false;
    }
}

// Lớp biểu diễn quần thể trong giải thuật di truyền
class Population {
    List<MonthlySchedule> schedules;

    public Population(int populationSize, int daysInMonth, int hoursPerDay) {
        schedules = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            schedules.add(new MonthlySchedule(daysInMonth, hoursPerDay));
        }
    }

    // Lấy lịch trình tốt nhất trong quần thể
    public MonthlySchedule getBestSchedule() {
        return Collections.min(schedules, Comparator.comparingDouble(Population::fitness));
    }

    // In ra tổng số giờ làm việc dư thừa của mỗi lịch trình
    public void printExcessHours() {
        for (int i = 0; i < schedules.size(); i++) {
            MonthlySchedule schedule = schedules.get(i);
            System.out.println("Schedule " + (i + 1) + " - Excess Hours: " + calculateExcessHours(schedule));
        }
    }

    // Tính tổng số giờ làm việc dư thừa cho một lịch trình làm việc trong tháng
    private static double calculateExcessHours(MonthlySchedule schedule) {
        double totalUnusedHours = 0;
        for (DailySchedule dailySchedule : schedule.scheduleMap.values()) {
            totalUnusedHours += dailySchedule.remainingHours;
        }
        return totalUnusedHours;
    }

    // Tính độ thích nghi của một lịch trình làm việc trong tháng
    private static double fitness(MonthlySchedule schedule) {
        return calculateExcessHours(schedule);
    }

    // Các phương thức khác như selectParent(), crossover(), mutate(), evolve() không thay đổi
    // Chọn một phần tử từ quần thể theo nguyên tắc chọn lọc tự nhiên
    private MonthlySchedule selectParent() {
        Random rand = new Random();
        double totalFitness = schedules.stream().mapToDouble(Population::fitness).sum();
        double r = rand.nextDouble() * totalFitness;
        double sum = 0;
        for (MonthlySchedule schedule : schedules) {
            sum += fitness(schedule);
            if (sum >= r) {
                return schedule;
            }
        }
        return schedules.get(rand.nextInt(schedules.size()));
    }

    // Lai ghép hai lịch trình cha mẹ để tạo ra một lịch trình con mới
    private MonthlySchedule crossover(MonthlySchedule parent1, MonthlySchedule parent2) {
        MonthlySchedule child = new MonthlySchedule(parent1.scheduleMap.size(), 8);
        for (int day = 1; day <= parent1.scheduleMap.size(); day++) {
            if (day % 2 == 0) {
                for (Job job : parent1.scheduleMap.get(day).jobs) {
                    child.addJob(day, job);
                }
            } else {
                for (Job job : parent2.scheduleMap.get(day).jobs) {
                    child.addJob(day, job);
                }
            }
        }
        return child;
    }

    // Đột biến một lịch trình
    private void mutate(MonthlySchedule schedule) {
        Random rand = new Random();
        for (int day = 1; day <= schedule.scheduleMap.size(); day++) {
            if (rand.nextDouble() < 0.1) { // xác suất đột biến là 10%
                schedule.scheduleMap.get(day).jobs.clear();
                schedule.scheduleMap.get(day).remainingHours = 8;
            }
        }
    }

    // Làm mới quần thể bằng cách lựa chọn, lai, và đột biến
    public void evolve() {
        List<MonthlySchedule> newGeneration = new ArrayList<>();
        // Chọn 50% lịch trình tốt nhất để lai ghép
        int eliteSize = schedules.size() / 2;
        for (int i = 0; i < eliteSize; i++) {
            newGeneration.add(schedules.get(i));
        }
        // Lai ghép các lịch trình tốt nhất để tạo ra các thế hệ mới
        while (newGeneration.size() < schedules.size()) {
            MonthlySchedule parent1 = selectParent();
            MonthlySchedule parent2 = selectParent();
            MonthlySchedule child = crossover(parent1, parent2);
            mutate(child);
            newGeneration.add(child);
        }
        schedules = newGeneration;
    }
}

class EmployeeScheduling {
    public static void main(String[] args) {
        // Định nghĩa các công việc
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("Job1", 4));
        jobs.add(new Job("Job2", 3));
        jobs.add(new Job("Job3", 5));
        jobs.add(new Job("Job4", 2));
        jobs.add(new Job("Job5", 6));
        jobs.add(new Job("Job6", 3));
        jobs.add(new Job("Job7", 4));
        jobs.add(new Job("Job8", 5));
        jobs.add(new Job("Job9", 3));
        jobs.add(new Job("Job10", 2));
        jobs.add(new Job("Job11", 4));
        jobs.add(new Job("Job12", 3));
        jobs.add(new Job("Job13", 5));
        jobs.add(new Job("Job14", 2));
        jobs.add(new Job("Job15", 6));
        jobs.add(new Job("Job16", 3));
        jobs.add(new Job("Job17", 4));
        jobs.add(new Job("Job18", 5));
        jobs.add(new Job("Job19", 3));
        jobs.add(new Job("Job20", 2));

        int populationSize = 50;
        int daysInMonth = 30;
        int hoursPerDay = 8;
        int generations = 1000;

        // Khởi tạo quần thể
        Population population = new Population(populationSize, daysInMonth, hoursPerDay);

        // Tiến hành tối ưu hóa qua nhiều thế hệ
        for (int i = 0; i < generations; i++) {
            population.evolve();
            if (i % 100 == 0) { // In kết quả sau mỗi 100 thế hệ
                System.out.println("Generation " + (i + 1));
                population.printExcessHours();
            }
        }

        // Lấy lịch trình tốt nhất sau khi tối ưu hóa
        MonthlySchedule bestSchedule = population.getBestSchedule();

        // In ra lịch trình làm việc của nhóm nhân viên
        for (int day = 1; day <= daysInMonth; day++) {
            System.out.println("Day " + day + ": " + bestSchedule.scheduleMap.get(day).jobs);
        }
    }
}
