/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Statistics;

/**
 *
 * @author kostas
 */
public class ABStats {

String Server_Software;
String Server_Hostname;
int Server_Port;
String Document_Path;
int Document_Length_bytes;
int Concurrency_Level;
double Time_taken_for_tests_seconds;
int Complete_requests;
int Failed_requests_number;
int Failed_requests_Connect;
int Failed_requests_Receive;
int Failed_requests_Length;
int Failed_requests_Exceptions;
int Non_2xx_responses;
int Keep_Alive_requests;
int Total_transferred_bytes;
int HTML_transferred_bytes;
double Requests_per_second_mean;
double Time_per_request_mean;
double Time_per_request_mean_across_all_concurrent_requests;
double Transfer_rate_received;
double Connection_Times_Connect_min;
double Connection_Times_Connect_mean;
double Connection_Times_Connect_sd;
double Connection_Times_Connect_median;
double Connection_Times_Connect_max;
double Connection_Times_Processing_min;
double Connection_Times_Processing_mean;
double Connection_Times_Processing_sd;
double Connection_Times_Processing_median;
double Connection_Times_Processing_max;
double Connection_Times_Waiting_min;
double Connection_Times_Waiting_mean;
double Connection_Times_Waiting_sd;
double Connection_Times_Waiting_median;
double Connection_Times_Waiting_max;
double Connection_Times_Total_min;
double Connection_Times_Total_mean;
double Connection_Times_Total_sd;
double Connection_Times_Total_median;
double Connection_Times_Total_max;
double Percentage_50;
double Percentage_66;
double Percentage_75;
double Percentage_80;
double Percentage_90;
double Percentage_95;
double Percentage_98;
double Percentage_99;
double Percentage_100;

    public String getServer_Software() {
        return Server_Software;
    }

    public void setServer_Software(String Server_Software) {
        this.Server_Software = Server_Software;
    }

    public String getServer_Hostname() {
        return Server_Hostname;
    }

    public void setServer_Hostname(String Server_Hostname) {
        this.Server_Hostname = Server_Hostname;
    }

    public int getServer_Port() {
        return Server_Port;
    }

    public void setServer_Port(int Server_Port) {
        this.Server_Port = Server_Port;
    }

    public String getDocument_Path() {
        return Document_Path;
    }

    public void setDocument_Path(String Document_Path) {
        this.Document_Path = Document_Path;
    }

    public int getDocument_Length_bytes() {
        return Document_Length_bytes;
    }

    public void setDocument_Length_bytes(int Document_Length_bytes) {
        this.Document_Length_bytes = Document_Length_bytes;
    }

    public int getConcurrency_Level() {
        return Concurrency_Level;
    }

    public void setConcurrency_Level(int Concurrency_Level) {
        this.Concurrency_Level = Concurrency_Level;
    }

    public double getTime_taken_for_tests_seconds() {
        return Time_taken_for_tests_seconds;
    }

    public void setTime_taken_for_tests_seconds(double Time_taken_for_tests_seconds) {
        this.Time_taken_for_tests_seconds = Time_taken_for_tests_seconds;
    }

    public int getComplete_requests() {
        return Complete_requests;
    }

    public void setComplete_requests(int Complete_requests) {
        this.Complete_requests = Complete_requests;
    }

    public int getFailed_requests_number() {
        return Failed_requests_number;
    }

    public void setFailed_requests_number(int Failed_requests_number) {
        this.Failed_requests_number = Failed_requests_number;
    }

    public int getFailed_requests_Connect() {
        return Failed_requests_Connect;
    }

    public void setFailed_requests_Connect(int Failed_requests_Connect) {
        this.Failed_requests_Connect = Failed_requests_Connect;
    }

    public int getFailed_requests_Receive() {
        return Failed_requests_Receive;
    }

    public void setFailed_requests_Receive(int Failed_requests_Receive) {
        this.Failed_requests_Receive = Failed_requests_Receive;
    }

    public int getFailed_requests_Length() {
        return Failed_requests_Length;
    }

    public void setFailed_requests_Length(int Failed_requests_Length) {
        this.Failed_requests_Length = Failed_requests_Length;
    }

    public int getFailed_requests_Exceptions() {
        return Failed_requests_Exceptions;
    }

    public void setFailed_requests_Exceptions(int Failed_requests_Exceptions) {
        this.Failed_requests_Exceptions = Failed_requests_Exceptions;
    }

    public int getNon_2xx_responses() {
        return Non_2xx_responses;
    }

    public void setNon_2xx_responses(int Non_2xx_responses) {
        this.Non_2xx_responses = Non_2xx_responses;
    }

    public int getKeep_Alive_requests() {
        return Keep_Alive_requests;
    }

    public void setKeep_Alive_requests(int Keep_Alive_requests) {
        this.Keep_Alive_requests = Keep_Alive_requests;
    }

    public int getTotal_transferred_bytes() {
        return Total_transferred_bytes;
    }

    public void setTotal_transferred_bytes(int Total_transferred_bytes) {
        this.Total_transferred_bytes = Total_transferred_bytes;
    }

    public int getHTML_transferred_bytes() {
        return HTML_transferred_bytes;
    }

    public void setHTML_transferred_bytes(int HTML_transferred_bytes) {
        this.HTML_transferred_bytes = HTML_transferred_bytes;
    }

    public double getRequests_per_second_mean() {
        return Requests_per_second_mean;
    }

    public void setRequests_per_second_mean(double Requests_per_second_mean) {
        this.Requests_per_second_mean = Requests_per_second_mean;
    }

    public double getTime_per_request_mean() {
        return Time_per_request_mean;
    }

    public void setTime_per_request_mean(double Time_per_request_mean) {
        this.Time_per_request_mean = Time_per_request_mean;
    }

    public double getTime_per_request_mean_across_all_concurrent_requests() {
        return Time_per_request_mean_across_all_concurrent_requests;
    }

    public void setTime_per_request_mean_across_all_concurrent_requests(double Time_per_request_mean_across_all_concurrent_requests) {
        this.Time_per_request_mean_across_all_concurrent_requests = Time_per_request_mean_across_all_concurrent_requests;
    }

    public double getTransfer_rate_received() {
        return Transfer_rate_received;
    }

    public void setTransfer_rate_received(double Transfer_rate_received) {
        this.Transfer_rate_received = Transfer_rate_received;
    }

    public double getConnection_Times_Connect_min() {
        return Connection_Times_Connect_min;
    }

    public void setConnection_Times_Connect_min(double Connection_Times_Connect_min) {
        this.Connection_Times_Connect_min = Connection_Times_Connect_min;
    }

    public double getConnection_Times_Connect_mean() {
        return Connection_Times_Connect_mean;
    }

    public void setConnection_Times_Connect_mean(double Connection_Times_Connect_mean) {
        this.Connection_Times_Connect_mean = Connection_Times_Connect_mean;
    }

    public double getConnection_Times_Connect_sd() {
        return Connection_Times_Connect_sd;
    }

    public void setConnection_Times_Connect_sd(double Connection_Times_Connect_sd) {
        this.Connection_Times_Connect_sd = Connection_Times_Connect_sd;
    }

    public double getConnection_Times_Connect_median() {
        return Connection_Times_Connect_median;
    }

    public void setConnection_Times_Connect_median(double Connection_Times_Connect_median) {
        this.Connection_Times_Connect_median = Connection_Times_Connect_median;
    }

    public double getConnection_Times_Connect_max() {
        return Connection_Times_Connect_max;
    }

    public void setConnection_Times_Connect_max(double Connection_Times_Connect_max) {
        this.Connection_Times_Connect_max = Connection_Times_Connect_max;
    }

    public double getConnection_Times_Processing_min() {
        return Connection_Times_Processing_min;
    }

    public void setConnection_Times_Processing_min(double Connection_Times_Processing_min) {
        this.Connection_Times_Processing_min = Connection_Times_Processing_min;
    }

    public double getConnection_Times_Processing_mean() {
        return Connection_Times_Processing_mean;
    }

    public void setConnection_Times_Processing_mean(double Connection_Times_Processing_mean) {
        this.Connection_Times_Processing_mean = Connection_Times_Processing_mean;
    }

    public double getConnection_Times_Processing_sd() {
        return Connection_Times_Processing_sd;
    }

    public void setConnection_Times_Processing_sd(double Connection_Times_Processing_sd) {
        this.Connection_Times_Processing_sd = Connection_Times_Processing_sd;
    }

    public double getConnection_Times_Processing_median() {
        return Connection_Times_Processing_median;
    }

    public void setConnection_Times_Processing_median(double Connection_Times_Processing_median) {
        this.Connection_Times_Processing_median = Connection_Times_Processing_median;
    }

    public double getConnection_Times_Processing_max() {
        return Connection_Times_Processing_max;
    }

    public void setConnection_Times_Processing_max(double Connection_Times_Processing_max) {
        this.Connection_Times_Processing_max = Connection_Times_Processing_max;
    }

    public double getConnection_Times_Waiting_min() {
        return Connection_Times_Waiting_min;
    }

    public void setConnection_Times_Waiting_min(double Connection_Times_Waiting_min) {
        this.Connection_Times_Waiting_min = Connection_Times_Waiting_min;
    }

    public double getConnection_Times_Waiting_mean() {
        return Connection_Times_Waiting_mean;
    }

    public void setConnection_Times_Waiting_mean(double Connection_Times_Waiting_mean) {
        this.Connection_Times_Waiting_mean = Connection_Times_Waiting_mean;
    }

    public double getConnection_Times_Waiting_sd() {
        return Connection_Times_Waiting_sd;
    }

    public void setConnection_Times_Waiting_sd(double Connection_Times_Waiting_sd) {
        this.Connection_Times_Waiting_sd = Connection_Times_Waiting_sd;
    }

    public double getConnection_Times_Waiting_median() {
        return Connection_Times_Waiting_median;
    }

    public void setConnection_Times_Waiting_median(double Connection_Times_Waiting_median) {
        this.Connection_Times_Waiting_median = Connection_Times_Waiting_median;
    }

    public double getConnection_Times_Waiting_max() {
        return Connection_Times_Waiting_max;
    }

    public void setConnection_Times_Waiting_max(double Connection_Times_Waiting_max) {
        this.Connection_Times_Waiting_max = Connection_Times_Waiting_max;
    }

    public double getConnection_Times_Total_min() {
        return Connection_Times_Total_min;
    }

    public void setConnection_Times_Total_min(double Connection_Times_Total_min) {
        this.Connection_Times_Total_min = Connection_Times_Total_min;
    }

    public double getConnection_Times_Total_mean() {
        return Connection_Times_Total_mean;
    }

    public void setConnection_Times_Total_mean(double Connection_Times_Total_mean) {
        this.Connection_Times_Total_mean = Connection_Times_Total_mean;
    }

    public double getConnection_Times_Total_sd() {
        return Connection_Times_Total_sd;
    }

    public void setConnection_Times_Total_sd(double Connection_Times_Total_sd) {
        this.Connection_Times_Total_sd = Connection_Times_Total_sd;
    }

    public double getConnection_Times_Total_median() {
        return Connection_Times_Total_median;
    }

    public void setConnection_Times_Total_median(double Connection_Times_Total_median) {
        this.Connection_Times_Total_median = Connection_Times_Total_median;
    }

    public double getConnection_Times_Total_max() {
        return Connection_Times_Total_max;
    }

    public void setConnection_Times_Total_max(double Connection_Times_Total_max) {
        this.Connection_Times_Total_max = Connection_Times_Total_max;
    }

    public double getPercentage_50() {
        return Percentage_50;
    }

    public void setPercentage_50(double Percentage_50) {
        this.Percentage_50 = Percentage_50;
    }

    public double getPercentage_66() {
        return Percentage_66;
    }

    public void setPercentage_66(double Percentage_66) {
        this.Percentage_66 = Percentage_66;
    }

    public double getPercentage_75() {
        return Percentage_75;
    }

    public void setPercentage_75(double Percentage_75) {
        this.Percentage_75 = Percentage_75;
    }

    public double getPercentage_80() {
        return Percentage_80;
    }

    public void setPercentage_80(double Percentage_80) {
        this.Percentage_80 = Percentage_80;
    }

    public double getPercentage_90() {
        return Percentage_90;
    }

    public void setPercentage_90(double Percentage_90) {
        this.Percentage_90 = Percentage_90;
    }

    public double getPercentage_95() {
        return Percentage_95;
    }

    public void setPercentage_95(double Percentage_95) {
        this.Percentage_95 = Percentage_95;
    }

    public double getPercentage_98() {
        return Percentage_98;
    }

    public void setPercentage_98(double Percentage_98) {
        this.Percentage_98 = Percentage_98;
    }

    public double getPercentage_99() {
        return Percentage_99;
    }

    public void setPercentage_99(double Percentage_99) {
        this.Percentage_99 = Percentage_99;
    }

    public double getPercentage_100() {
        return Percentage_100;
    }

    public void setPercentage_100(double Percentage_100) {
        this.Percentage_100 = Percentage_100;
    }





}
