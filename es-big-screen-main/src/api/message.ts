export interface Message {
    id: number;
    systemId: number | null;
    systemName: string;
    fromRole: string;
    toRole: string;
    text: string;
    type: string;
    statue: number;
    time: string;
}