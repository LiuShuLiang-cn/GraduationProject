export interface DeployItem {
    cg: number;
    cgLat: number;
    cgLng: number;
    ga: number;
    gaLat: number;
    gaLng: number;
    id: number;
    jj: number;
    jjLat: number;
    jjLng: number;
    sysName: string;
    zyz: number;
    zyzLat: number;
    zyzLng: number;
}

export interface NumberOfPeopleItem {
    area: number;
    id: number;
    latitude: string;
    longitude: string;
    number: number;
    region: string;
    status: string;
    systemName: string;
    vicinity: string;
}

export interface OperateInfo {
    busA: string;
    busB: string;
    busC: string;
    busD: string;
    id: number;
    statusActivity: string;
    subwayA: string;
    subwayB: string;
    subwayC: string;
    subwayD: string;
    systemName: string;
}

export interface DataResponse {
    deployList: DeployItem[];
    numberOfPeopleList: NumberOfPeopleItem[];
    operate: OperateInfo;
    systemTime: string;
}