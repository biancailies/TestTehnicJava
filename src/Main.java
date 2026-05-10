import Model.Repository.BookingRepository;
import Model.Repository.RouteRepository;
import Model.Repository.StationRepository;
import Model.Repository.TrainRepository;
import Service.AdminService;
import Service.BookingService;
import Service.EmailService;
import Service.SearchService;
import Controller.AppController;
import Service.RouteOptimizerService;

public class Main {
    public static void main(String[] args) {
        // Initialize Repositories
        RouteRepository routeRepository = new RouteRepository();
        TrainRepository trainRepository = new TrainRepository();
        BookingRepository bookingRepository = new BookingRepository();
        StationRepository stationRepository = new StationRepository();

        // Initialize Services
        EmailService emailService = new EmailService();
        AdminService adminService = new AdminService(routeRepository, trainRepository,
                bookingRepository, stationRepository, emailService);
        BookingService bookingService = new BookingService(bookingRepository, trainRepository, emailService);
        SearchService searchService = new SearchService(trainRepository);

        // Route optimizer
        RouteOptimizerService routeOptimizerService = new RouteOptimizerService(trainRepository);
        AppController appController = new AppController(
                adminService,
                bookingService,
                searchService,
                routeOptimizerService
        );

        appController.runDemo();
    }
}
