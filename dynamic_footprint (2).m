% 83,4kg object dynamic footprint data
% from measured and exported csv file

file = "19-05-2025_22-00-18(83).csv"; % plik do analizy pomiarów
M = readmatrix(file);
M = M(:, ~all(isnan(M), 1)); % Removes columns with NaN

time = abs(M(:,1)); % takes only time intervals data
values = M(:, 2:end); % takes only adc pressure values data


macierz_SUPER = reshape(values(3352,:), 16, 16); % 6457, 9510, 14097 - ten chyba najepeiej?
                                                   % 3352 - też super ale
                                                   % ucięty kawałek
yvalues = {16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
xvalues = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
%h = heatmap(xvalues,yvalues,macierz_SUPER, 'Colormap', jet)
%h.XLabel = {'kolumny'};
%h.YLabel = {'wiersze'};



% Kalman filter - dla danych czasowych nie do wygładzania poszczególnych
% "klatek" pomiaru
%data = cat(3, M, M2, M3);  % data: 16 x 16 x 3

num_frames = size(values,1);
all_values_M = zeros(16,16,num_frames);
for k=1:num_frames
    all_values_M(:,:,k) = reshape(values(k,:),16,16);
end

writematrix(values,'values.csv');
%M1 = reshape(values(3352,:), 16, 16);
%M2 = reshape(values(9510,:), 16, 16);
%M3 = reshape(values(14097,:), 16, 16);

% Parametry filtra
Q = 1e-3;  % wariancja szumu procesu
R = 1;     % wariancja szumu pomiarowego

% Przygotuj dane
data = all_values_M;
[nx, ny, nt] = size(data);

% Macierz na wynik
data_filtered = zeros(size(data));

for i = 1:nx
    for j = 1:ny
        z = squeeze(data(i,j,:));  % pomiary w czasie
        data_filtered(i,j,:) = kalman1d(z, Q, R);
    end
end
function x_filtered = kalman1d(z, Q, R)
    n = length(z);
    x_filtered = zeros(size(z));
    
    % Inicjalizacja
    x_est = z(1);
    P = 1;

    for k = 1:n
        % Predykcja
        x_pred = x_est;
        P_pred = P + Q;

        % Aktualizacja
        K = P_pred / (P_pred + R);
        x_est = x_pred + K * (z(k) - x_pred);
        P = (1 - K) * P_pred;

        x_filtered(k) = x_est;
    end
end
plot(squeeze(data(8,8,:)), 'r'); hold on
plot(squeeze(data_filtered(8,8,:)), 'b')
legend('Oryginał', 'Po filtrze Kalmana');
title('Punkt (8,8) w czasie');
