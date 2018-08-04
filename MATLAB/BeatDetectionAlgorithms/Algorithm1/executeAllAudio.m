 [parentdir,~,~]=fileparts(pwd);
 [parentdir,~,~]=fileparts(parentdir);
 [parentdir,~,~]=fileparts(parentdir);
baselocation = strcat (parentdir, '\documents\audioFiles\');
bpms = [20,60,100,120,200,240];

for i = 1 : length(bpms)
    file = strcat(baselocation,'tamborine_', num2str(bpms(i)), 'bpm_1-4time_', num2str(bpms(i)+1), 'beats_stereo.mp3');
    beat = control(file);
    fprintf('BPM should be %i, is: %2f\n', bpms(i),beat);
    fprintf('done One');
end


fprintf('Starting  song Test\n');

songs1 = strcat(baselocation, 'Maroon5_Sugar_drum_Cover_trimmed.mp3');
songs2 =  strcat(baselocation, 'TeddybearsSthlm_HeyBoy_trimmedToOneMinute.mp3');

bpms = [120, 200];

tic;
    % change next Line to your function
beat = control(songs1);
elapsed = toc;
fprintf('BPM should be %i, is: %5.2f \tduration: %5.3f\n', bpms(1),beat, elapsed);
tic;
    % change next Line to your function
beat = control(songs1);
elapsed = toc;
fprintf('BPM should be %i, is: %5.2f \tduration: %5.3f\n', bpms(2),beat, elapsed);