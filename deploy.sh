#!/bin/bash

#export user_consola_dmgr=s32338
#export pwd_consola_dmgr=3nicoleUPC12@
export nro_srt=SRT-0000-00000
export nro_oc=00000
export name_folder=/Apps/pases/scripts
export name_folder_ears=$WORKSPACE/digital-channel-ffoo/digital-channel-ffoo-ear/target
export now_time=$(date +%Y%m%d%H%M%S)


export path_backup=$name_folder/$now_time
export path_log=$name_folder/log_$now_time.txt
export path_log_was=$path_backup/${nro_srt}-${nro_oc}.txt
export job_name=${nro_srt}-${nro_oc}
#export dmgr_port=8880
#export dmgr_host=s423vd2


#1: DESA / 2: UAT / 3: PRD1 / 4: PRD2
#environment=1

#echo $environment

echo "**************************************************" | tee -a $path_log
echo "Start Script: $now_time - environment: $environment" | tee -a $path_log
echo "**************************************************" | tee -a $path_log

if [ $environment == 1 ]; then
	echo "**************************************************"
	echo "DESARROLLO"
	echo "**************************************************"
	echo $cell_name
	echo $cluster_name
	echo $name_profile_dmgr
	echo $serverIHS
	#export cell_name=s423vd2Cell02
	#export cluster_name=ClusterBMOVIL_DES
	#export name_profile_dmgr=AppSrv01
	##export serverIHS='s341vd2','ihs_bmovil'-'s341vd3','ihs_bmovil'-'s341vd4','ihs_bmovil'
	#export serverIHS='s341vd2','ihs_bmovil'
fi

if [ $environment == 2 ]; then
	echo "**************************************************"
	echo "UAT"
	echo "**************************************************"
	echo $cell_name
	echo $cluster_name
	echo $name_profile_dmgr
	echo $serverIHS
	#export cell_name=s423va2Cell01
	#export cluster_name=ClusterBMOVIL_UAT
	#export name_profile_dmgr=dmgr 
	##export serverIHS='s341vd2','ihs_bmovil'-'s341vd3','ihs_bmovil'-'s341vd4','ihs_bmovil'
	#export serverIHS='s227va5','ihs_bmovil'
fi

if [ $environment == 3 ]; then
	echo "**************************************************"
	echo "PRD - SITE 1"
	echo "**************************************************"
	export cell_name=s423vp2Cell01
	export cluster_name=ClusterBMOVIL_PRD
	export name_profile_dmgr=dmgr
	export serverIHS='s227vp7','ihs_bmovil2'-'s227vp5','ihs_bmovil'
fi

if [ $environment == 4 ]; then
	echo "**************************************************"
	echo "PRD - SITE 2"
	echo "**************************************************"
	export cell_name=s423vp2Cell02
	export cluster_name=ClusterBMOVIL_PRD
	export name_profile_dmgr=dmgr2
	export serverIHS='s227vp11','ihs_bmovil2'-'s227vp9','ihs_bmovil'
fi

export PATH_WASADMIN=/opt/IBM/WebSphere/AppServer/profiles/$name_profile_dmgr/bin

echo "**************************************************"

echo "**************************************************"


export app_1_backup=$path_backup/digital-channel-ffoo-ear.ear
export app_1_name=digital-channel-ffoo-ear
export app_1_path=$name_folder_ears/digital-channel-ffoo-ear.ear
export app_1_sharedLibraryName=ffoo_service_lib
export app_1_sharedLibraryPath=/Apps/FFOOService/lib
export app_1_sharedLibraryDescription='library digital channel ffoo project'


echo "crear carpeta bkp: $path_backup"
sudo mkdir -p $path_backup

echo "Owner a carpeta $name_folder"
sudo chown root $name_folder -R

echo "Permisos a carpeta $name_folder"
sudo chmod 777 $name_folder -R

#Detener app
sudo $PATH_WASADMIN/wsadmin.sh -username $user_consola_dmgr -password $pwd_consola_dmgr -conntype SOAP -port $dmgr_port -host $dmgr_host -jobid $job_name -tracefile $path_log_was -appendtrace true -lang jython -f "$name_folder/stopApp.py" "$cell_name" "$cluster_name" "$app_1_name" | tee -a $path_log

#Instalar app
sudo $PATH_WASADMIN/wsadmin.sh -username $user_consola_dmgr -password $pwd_consola_dmgr -conntype SOAP -port $dmgr_port -host $dmgr_host -jobid $job_name -tracefile $path_log_was -appendtrace true -lang jython -f "$name_folder/installApp.py" "$cell_name" "$cluster_name" "$app_1_name" "$app_1_path" "$app_1_sharedLibraryName" "$app_1_sharedLibraryPath" "$app_1_sharedLibraryDescription" "$app_1_backup" $serverIHS | tee -a $path_log

#Sincronizar nodos
sudo $PATH_WASADMIN/wsadmin.sh -username $user_consola_dmgr -password $pwd_consola_dmgr -conntype SOAP -port $dmgr_port -host $dmgr_host -jobid $job_name -tracefile $path_log_was -appendtrace true -lang jython -f "$name_folder/synCluster.py" "$cell_name" "$cluster_name" | tee -a $path_log

#Iniciar app
echo "Start Apps"
sudo $PATH_WASADMIN/wsadmin.sh -username $user_consola_dmgr -password $pwd_consola_dmgr -conntype SOAP -port $dmgr_port -host $dmgr_host -jobid $job_name -tracefile $path_log_was -appendtrace true -lang jython -f "$name_folder/startApp.py" "$cell_name" "$cluster_name" "$app_1_name" | tee -a $path_log

echo "**************************************************" | tee -a $path_log
echo "End Script: $now_time - environment: $environment" | tee -a $path_log
echo "**************************************************" | tee -a $path_log
